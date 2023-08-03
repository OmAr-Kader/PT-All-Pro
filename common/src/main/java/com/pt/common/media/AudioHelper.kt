@file:Suppress("LongLine")

package com.pt.common.media

import android.content.ContentUris.withAppendedId
import android.provider.MediaStore.Audio.*
import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.stable.*

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.getMusicDetails(
    path: String,
    load: Boolean,
): MusicSack = justCoroutineCur {
    MusicSack(
        android.provider.MediaStore.UNKNOWN_STRING,
        com.pt.common.global.FileLate(path).extension
    ).runSusBack {
        return@runSusBack withBackDef(this@runSusBack) {
            return@withBackDef runCatching {
                com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                    setDataSource(path)
                    return@useSusMeta extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_TITLE
                    ).letSusBack {
                        return@letSusBack if (it.isNullOrEmpty())
                            FileLate(path).fetchFileName
                        else
                            it
                    }.letSusBack { tit ->
                        return@letSusBack ((extractMetadata(
                            android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                        )?.toLong()) ?: 0L).letSusBack { d ->
                            if (load) {
                                contentResolver.findIDForArtist(
                                    path,
                                    FileLate(path).extension
                                ) {}.runSusBack {
                                    MusicSack(
                                        title = tit,
                                        artist = this,
                                        dur_NSongs = d
                                    )
                                }
                            } else {
                                MusicSack(title = tit, dur_NSongs = d)
                            }
                        }
                    }
                }
            }.getOrDefault(this@runSusBack)
        }
    }
}

@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.getMusicDuration(
    path: String,
): Long = justCoroutineCur {
    withBackCurDef(null) {
        catchy(null) {
            com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                setDataSource(path)
                ((extractMetadata(
                    android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                )?.toLong()) ?: 0L).letSusBack { d ->
                    contentResolver.findIDForArtist(
                        path,
                        com.pt.common.global.FileLate(path).extension
                    ) {}.runSusBack {
                        d
                    }
                }
            }
        }
    }
} ?: 0L

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.firstAudioSong(
    album: String,
): String? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        return@withBackCurDef catchy(null) {
            return@catchy query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH),
                AudioColumns.ALBUM + " " + DATA_EQUAL,
                arrayOf(album),
                null
            )?.useBack {
                if (moveToFirst()) {
                    curStr(PATH)
                } else {
                    null
                }
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allAudioLoader(
    isVo: Boolean,
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val songs: MutableList<MusicSack> = mutableListOf()
            val aw = if (isVo) {
                null
            } else {
                (AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
            }

            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, AudioColumns._ID, Media.TITLE, Media.ARTIST, AudioColumns.ARTIST_ID),
                aw,
                if (isVo) null else arrayOf(MUSIC_OGG, MUSIC_OPUS, "0"),
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                while (moveToNext()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = curStr(Media.ARTIST),
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_ALL_SONGS,
                            musicUri = withAppendedId(
                                Media.EXTERNAL_CONTENT_URI,
                                curLong(AudioColumns._ID)
                            ).toString()
                        ).alsoSusBack(songs::add)
                    }
                }
            }
            return@runCatching songs
        }.getOrElse {
            allAudioLoaderSecond(isVo)
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allAudioLoaderSecond(
    isVo: Boolean,
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef catchy(mutableListOf()) {
            val songs: MutableList<MusicSack> = mutableListOf()
            val aw = if (isVo) {
                null
            } else {
                (AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
            }
            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, Media.TITLE, AudioColumns.ARTIST_ID, AudioColumns.ARTIST),
                aw,
                if (isVo) null else arrayOf(MUSIC_OGG, MUSIC_OPUS, "0"),
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                while (moveToNext()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = curStr(AudioColumns.ARTIST).toStr,
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_ALL_SONGS
                        ).alsoSusBack(songs::add)
                    }
                }
            }
            return@catchy songs
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.fetchAudioLoader(
): MusicSack? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        return@withBackCurDef runCatching {
            val aw = (AudioColumns.MIME_TYPE + " " + DATA_EQUAL + " " + DATA_AND + " " +
                    AudioColumns.SIZE + " " + DATA_MORE)
            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, Media.TITLE, Media.ARTIST, AudioColumns.ARTIST_ID),
                aw,
                arrayOf(MUSIC_FIELD, "50000"),
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                if (moveToFirst()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = curStr(Media.ARTIST),
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_YOUR_PLAYLIST
                        )
                    }
                } else {
                    null
                }
            }
        }.getOrElse {
            fetchAudioLoaderSecond()
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
fun android.content.ContentResolver.fetchAudioLoaderNormal(
): MusicSack? = catchy(null) {
    val aw = (AudioColumns.MIME_TYPE + " " + DATA_EQUAL + " " + DATA_AND + " " +
            AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
    query(
        Media.EXTERNAL_CONTENT_URI,
        arrayOf(PATH, Media.TITLE, AudioColumns.ARTIST_ID, AudioColumns.ARTIST),
        aw,
        arrayOf(MUSIC_FIELD, "0"),
        AudioColumns.DATE_MODIFIED + " " + DATA_DES,
    )?.use {
        if (it.moveToFirst()) {
            MusicSack(
                title = (it.curStr(Media.TITLE) ?: it.curName),
                artist = it.curInt(AudioColumns.ARTIST).toStr,
                pathAudio = it.curStr(PATH),
                idArtAlb = it.curInt(AudioColumns.ARTIST_ID),
                songType = IS_YOUR_PLAYLIST
            )
        } else {
            null
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.fetchAudioLoaderSecond(
): MusicSack? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        return@withBackCurDef catchy(null) {
            val aw = (AudioColumns.MIME_TYPE + " " + DATA_EQUAL + " " + DATA_AND + " " +
                    AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, Media.TITLE, AudioColumns.ARTIST_ID, AudioColumns.ARTIST),
                aw,
                arrayOf(MUSIC_FIELD, "0"),
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                if (moveToFirst()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = curInt(AudioColumns.ARTIST).toStr,
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_YOUR_PLAYLIST
                        )
                    }
                } else {
                    null
                }
            }
        }
    }

}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.filterAudioByArtist(
    art: String,
    isVo: Boolean,
    artist: String?,
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef catchy(mutableListOf()) {
            val songs: MutableList<MusicSack> = mutableListOf()
            val sel = if (isVo) {
                AudioColumns.ARTIST + " " + DATA_EQUAL
            } else {
                (AudioColumns.ARTIST + " " + DATA_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
            }
            val selArg = if (isVo) {
                arrayOf(art)
            } else {
                arrayOf(art, MUSIC_OGG, MUSIC_OPUS, "0")
            }
            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, Media.TITLE, AudioColumns.ARTIST_ID),
                sel,
                selArg,
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                while (moveToNext()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = artist,
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_ALL_SONGS
                        ).alsoSusBack(songs::add)
                    }
                }
            }
            return@catchy songs
        }
    }
}


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.filterAudioByAlbum(
    art: String,
    isVo: Boolean,
    artist: String?,
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef catchy(mutableListOf()) {
            val songs: MutableList<MusicSack> = mutableListOf()
            val sel = if (isVo) {
                AudioColumns.ALBUM + " " + DATA_EQUAL
            } else {
                (AudioColumns.ALBUM + " " + DATA_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.MIME_TYPE + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                        AudioColumns.SIZE + " " + DATA_NOT_EQUAL)
            }
            val selArg = if (isVo) {
                arrayOf(art)
            } else {
                arrayOf(art, MUSIC_OGG, MUSIC_OPUS, "0")
            }
            query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, Media.TITLE, AudioColumns.ARTIST_ID),
                sel,
                selArg,
                AudioColumns.DATE_MODIFIED + " " + DATA_DES,
            )?.useBack {
                while (moveToNext()) {
                    (curStr(Media.TITLE) ?: curName).letSusBack { itN ->
                        MusicSack(
                            title = itN,
                            artist = artist,
                            pathAudio = curStr(PATH),
                            idArtAlb = curInt(AudioColumns.ARTIST_ID),
                            songType = IS_ALL_SONGS
                        ).alsoSusBack(songs::add)
                    }
                }
            }
            return@catchy songs
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun MutableList<MusicSack>.margeArtistList(
    cont: android.content.ContentResolver?,
): MutableList<MusicSack> = justCoroutine {
    val s = android.provider.MediaStore.UNKNOWN_STRING.toStr
    withBackDef(this@margeArtistList) {
        this@margeArtistList.onEachSusBack(cont) {
            if (cont != null && (artist == null || artist.toStr == s)) {
                cont.findOneSong(
                    this@onEachSusBack.pathAudio.toStr
                )?.letSusBack { (title, artist, _, _, _, _, _, _, _, _) ->
                    this@onEachSusBack.artist = artist ?: cont.findIDForArtist(
                        this@onEachSusBack.pathAudio, this@onEachSusBack.artist.toStr
                    ) {}
                    this@onEachSusBack.title = title ?: cont.findOneTitle(
                        this@onEachSusBack.pathAudio.toStr
                    )
                } ?: this@onEachSusBack.runSusBack {
                    artist = cont.findIDForArtist(pathAudio, artist.toStr) {}
                }
            }
        }
        return@withBackDef this@margeArtistList
    }
}

suspend fun android.content.ContentResolver.getAudioPathFromURI(
    uri: String?,
): String? = justCoroutine {
    return@justCoroutine withBackCurDef(null) {
        query(uri.toUri, arrayOf(PATH), null, null, null)?.useBack {
            if (moveToFirst()) {
                curStr(PATH)
            } else {
                null
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.findIDForArtist(
    path: String?,
    art: String,
    s: (String) -> Unit
): String = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(art) {
        if (!FileLate(path.toStr).exists()) {
            s.invoke("")
            return@withBackCurDef ""
        }

        return@withBackCurDef catchy(art) {
            return@catchy query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(PATH, AudioColumns.ARTIST),
                PATH.toStr + " " + DATA_EQUAL,
                arrayOf(path.toStr),
                null
            )?.useBack {
                if (moveToFirst()) {
                    return@useBack curStr(AudioColumns.ARTIST).toStr.letSusBack {
                        if (it == android.provider.MediaStore.UNKNOWN_STRING) {
                            if (art == DATA_NULL) path.exTenSion else art
                        } else {
                            it
                        }
                    }
                } else {
                    if (art == DATA_NULL) path.exTenSion else art
                }
            } ?: if (art == DATA_NULL) path.exTenSion else art
        }
    }.also(s)
}

inline val String?.exTenSion: String
    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    get() = com.pt.common.global.FileLate(this.toStr).extension.trim()

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allArtistsLoader(
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef catchy(mutableListOf()) {
            val songs: MutableList<MusicSack> = mutableListOf()
            query(
                Artists.EXTERNAL_CONTENT_URI,
                arrayOf(Artists.ARTIST, Artists.NUMBER_OF_TRACKS, Artists._ID),
                null,
                null,
                Artists.NUMBER_OF_TRACKS + " " + DATA_DES
            )?.useBack {
                while (moveToNext()) {
                    MusicSack(
                        title = curStr(Artists.ARTIST),
                        artist = curStr(Artists.ARTIST),
                        dur_NSongs = curInt(Artists.NUMBER_OF_TRACKS).toLong(),
                        idArtAlb = curInt(Artists._ID),
                        songType = ARTIST_IS
                    ).alsoSusBack(songs::add)
                }
            }
            return@catchy songs
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allAlbumsLoader(
): MutableList<MusicSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef catchy(mutableListOf()) {
            val songs: MutableList<MusicSack> = mutableListOf()
            query(
                Albums.EXTERNAL_CONTENT_URI,
                arrayOf(
                    Albums.ALBUM,
                    Albums.NUMBER_OF_SONGS,
                    Albums._ID,
                    ART,
                    Albums.ARTIST
                ),
                null,
                null,
                Albums.NUMBER_OF_SONGS + " " + DATA_DES
            )?.useBack {
                while (moveToNext()) {
                    MusicSack(
                        title = curStr(Albums.ALBUM),
                        artist = curStr(Albums.ARTIST),
                        pathAudio = curStr(ART),
                        dur_NSongs = curInt(Albums.NUMBER_OF_SONGS).toLong(),
                        idArtAlb = curInt(Albums._ID),
                        songType = ALBUM_IS,
                        album = curStr(Albums.ALBUM),
                    ).alsoSusBack(songs::add)
                }
            }
            return@catchy songs
        }
    }
}

@com.pt.common.global.WorkerAnn
fun android.content.Context.openAudioEffectSession(sessionId: Int, pac: String) {
    android.content.Intent(
        android.media.audiofx.AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION
    ).apply {
        putExtra(android.media.audiofx.AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
        putExtra(
            android.media.audiofx.AudioEffect.EXTRA_PACKAGE_NAME,
            pac
        )
        putExtra(
            android.media.audiofx.AudioEffect.EXTRA_CONTENT_TYPE,
            android.media.audiofx.AudioEffect.CONTENT_TYPE_MUSIC
        )

    }.also(::sendBroadcast)
}

@com.pt.common.global.WorkerAnn
fun android.content.Context.closeAudioEffectSession(sessionId: Int, pac: String) {
    android.content.Intent(
        android.media.audiofx.AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION
    ).apply {
        putExtra(android.media.audiofx.AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
        putExtra(
            android.media.audiofx.AudioEffect.EXTRA_PACKAGE_NAME,
            pac
        )
        putExtra(
            android.media.audiofx.AudioEffect.EXTRA_CONTENT_TYPE,
            android.media.audiofx.AudioEffect.CONTENT_TYPE_MUSIC
        )

    }.also(::sendBroadcast)

}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.findOneTitle(
    path: String,
): String? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        return@withBackCurDef query(
            Media.EXTERNAL_CONTENT_URI,
            arrayOf(Media.TITLE),
            PATH.toStr + " " + DATA_EQUAL,
            arrayOf(path),
            null,
        )?.useBack {
            if (moveToFirst()) {
                curStr(Media.TITLE)
            } else {
                null
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.findOneSong(
    path: String,
): MusicSack? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        return@withBackCurDef catchy(null) {
            return@catchy query(
                Media.EXTERNAL_CONTENT_URI,
                arrayOf(Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DURATION),
                PATH.toStr + " " + DATA_EQUAL,
                arrayOf(path),
                null,
            )?.useBack {
                if (moveToFirst()) {
                    return@useBack MusicSack(
                        title = curStr(Media.TITLE),
                        pathAudio = path,
                        artist = curStr(Media.ARTIST),
                        album = curStr(Media.ALBUM),
                        dur_NSongs = curLong(Media.DURATION)
                    )
                } else {
                    return@useBack null
                }
            }
        }
    }
}

suspend fun MusicSack.editSongTags(a: suspend () -> Unit) {
    withBackDef(Unit) {
        org.jaudiotagger.audio.AudioFileIO.read(
            FileLate(pathAudio ?: "")
        ).applySusBack {
            org.jaudiotagger.tag.TagOptionSingleton.getInstance().isAndroid = true
            tagOrCreateAndSetDefault.alsoSusBack {
                it.setField(org.jaudiotagger.tag.FieldKey.ARTIST, artist)
                it.setField(org.jaudiotagger.tag.FieldKey.ALBUM, album)
                it.setField(org.jaudiotagger.tag.FieldKey.TITLE, title)
            }
        }.alsoSusBack {
            it.commit()
        }
        a.invoke()
    }
}

suspend fun FileLate.editSongImage(artFile: FileLate, a: suspend () -> Unit): Boolean =
    justCoroutine {
        return@justCoroutine withBackDef(true) {
            return@withBackDef catchySus(true) {
                org.jaudiotagger.audio.AudioFileIO.read(this@editSongImage).apply {
                    org.jaudiotagger.tag.TagOptionSingleton.getInstance().isAndroid = true
                    tagOrCreateAndSetDefault.also {
                        it.deleteArtworkField()
                        org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile(
                            artFile
                        ).let { artWork ->
                            it.setField(artWork)
                        }
                    }
                }.alsoSusBack {
                    it.commit()
                }
                a.invoke()
                return@catchySus false
            }
        }
    }