package com.pt.pro.notepad.recorder

import android.media.*
import com.pt.pro.notepad.objects.BIT_RATE
import com.pt.pro.notepad.objects.CHANNELS
import com.pt.pro.notepad.objects.SAMPLE_RATE
import com.pt.pro.notepad.objects.SAMPLE_RATE_INDEX
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import kotlin.experimental.or

@Suppress("HardCodedStringLiteral")
class AudioRecordThread @Throws(IOException::class) constructor(
    private val outputStream: OutputStream?,
    private val onRecorderFailedListener: OnRecorderFailedListener?,
    source: Int,
) : () -> Unit {

    private var bufferSize = 0
    private var mediaCodec: MediaCodec? = null
    private var audioRecord: AudioRecord? = null


    init {
        bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        audioRecord = createAudioRecord(bufferSize, source)
        mediaCodec = createMediaCodec(bufferSize)
        mediaCodec!!.start()
        try {
            audioRecord!!.startRecording()
        } catch (e: Exception) {
            mediaCodec!!.release()
            throw IOException(e)
        }
    }

    @Suppress("DEPRECATION")
    override fun invoke() {
        onRecorderFailedListener?.onRecorderStarted()
        val bufferInfo = MediaCodec.BufferInfo()
        val codecInputBuffers = (mediaCodec ?: return).inputBuffers
        val codecOutputBuffers = (mediaCodec ?: return).outputBuffers
        try {
            while (!Thread.interrupted()) {
                val success = handleCodecInput(
                    audioRecord,
                    mediaCodec,
                    codecInputBuffers,
                    Thread.currentThread().isAlive
                )
                if (success) handleCodecOutput(
                    mediaCodec,
                    codecOutputBuffers,
                    bufferInfo,
                    outputStream
                )
            }
        } catch (_: IOException) {
        } finally {
            mediaCodec?.stop()
            audioRecord?.stop()
            mediaCodec?.release()
            audioRecord?.release()
            outputStream.runCatching {
                this?.close()
            }
        }
    }


    @Throws(IOException::class)
    private fun handleCodecInput(
        audioRecord: AudioRecord?,
        mediaCodec: MediaCodec?,
        codecInputBuffers: Array<ByteBuffer>,
        running: Boolean,
    ): Boolean {
        val audioRecordData = ByteArray(bufferSize)
        val length = audioRecord!!.read(audioRecordData, 0, audioRecordData.size)
        if (length == AudioRecord.ERROR_BAD_VALUE ||
            length == AudioRecord.ERROR_INVALID_OPERATION || length != bufferSize
        ) {
            if (length != bufferSize) {
                onRecorderFailedListener?.onRecorderFailed()
                return false
            }
        }
        val codecInputBufferIndex = mediaCodec!!.dequeueInputBuffer((10 * 1000).toLong())
        if (codecInputBufferIndex >= 0) {
            val codecBuffer = codecInputBuffers[codecInputBufferIndex]
            codecBuffer.clear()
            codecBuffer.put(audioRecordData)
            mediaCodec.queueInputBuffer(
                codecInputBufferIndex,
                0,
                length,
                0,
                if (running) 0 else MediaCodec.BUFFER_FLAG_END_OF_STREAM
            )
        }
        return true
    }

    @Suppress("DEPRECATION")
    @Throws(IOException::class)
    private fun handleCodecOutput(
        mediaCodec: MediaCodec?,
        codecOutputBuffers: Array<ByteBuffer>,
        bufferInfo: MediaCodec.BufferInfo,
        outputStream: OutputStream?,
    ) {
        var codecOutBuffers = codecOutputBuffers
        var codecOutputBufferIndex = (mediaCodec ?: return).dequeueOutputBuffer(bufferInfo, 0)
        while (codecOutputBufferIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
            if (codecOutputBufferIndex >= 0) {
                val encoderOutputBuffer = codecOutBuffers[codecOutputBufferIndex]
                encoderOutputBuffer.position(bufferInfo.offset)
                encoderOutputBuffer.limit(bufferInfo.offset + bufferInfo.size)
                if (bufferInfo.flags and
                    MediaCodec.BUFFER_FLAG_CODEC_CONFIG != MediaCodec.BUFFER_FLAG_CODEC_CONFIG
                ) {
                    val header = createAdsHeader(bufferInfo.size - bufferInfo.offset)
                    outputStream?.write(header)
                    val data = ByteArray(encoderOutputBuffer.remaining())
                    encoderOutputBuffer[data]
                    outputStream?.write(data)
                }
                encoderOutputBuffer.clear()
                mediaCodec.releaseOutputBuffer(codecOutputBufferIndex, false)
            } else if (codecOutputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                codecOutBuffers = mediaCodec.outputBuffers
            }
            codecOutputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0)
        }
    }


    private fun createAdsHeader(length: Int): ByteArray {
        val frameLength = length + 7
        val adHeader = ByteArray(7)
        adHeader[0] = 0xFF.toByte() // Sync Word
        adHeader[1] = 0xF1.toByte() // M PEG-4, Layer (0), No CRC
        adHeader[2] = (MediaCodecInfo.CodecProfileLevel.AACObjectLC - 1 shl 6).toByte()
        adHeader[2] = adHeader[2] or ((SAMPLE_RATE_INDEX shl 2).toByte())
        adHeader[2] = adHeader[2] or ((CHANNELS shr 2).toByte())
        adHeader[3] = (CHANNELS and 3 shl 6 or (frameLength shr 11 and 0x03)).toByte()
        adHeader[4] = (frameLength shr 3 and 0xFF).toByte()
        adHeader[5] = (frameLength and 0x07 shl 5 or 0x1f).toByte()
        adHeader[6] = 0xFC.toByte()
        return adHeader
    }

    @android.annotation.SuppressLint("MissingPermission")
    private fun createAudioRecord(bufferSize: Int, source: Int): AudioRecord {
        val audioRecord = AudioRecord(
            source, SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10
        )
        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            throw RuntimeException("Unable to initialize AudioRecord")
        }
        return audioRecord
    }

    @Suppress("SpellCheckingInspection")
    @Throws(IOException::class)
    private fun createMediaCodec(bufferSize: Int): MediaCodec {
        val mediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm")
        val mediaFormat = MediaFormat()
        mediaFormat.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm")
        mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE)
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, CHANNELS)
        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, bufferSize)
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE)
        mediaFormat.setInteger(
            MediaFormat.KEY_AAC_PROFILE,
            MediaCodecInfo.CodecProfileLevel.AACObjectLC
        )
        try {
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        } catch (e: Exception) {
            mediaCodec.release()
            throw IOException(e)
        }
        return mediaCodec
    }

    interface OnRecorderFailedListener {
        fun onRecorderFailed()
        fun onRecorderStarted()
    }
}