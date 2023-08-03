package com.pt.common.mutual.pref

class DbPrefInitializer : androidx.startup.Initializer<DBPreference> {

    override fun create(
        context: android.content.Context
    ): DBPreference {
        return context.pref
    }

    override fun dependencies(): List<Class<out androidx.startup.Initializer<*>>> = emptyList()

    companion object {

        @Volatile
        @JvmStatic
        internal var preference: DBPreference? = null
            set(value) {
                if (value == null) {
                    field?.readableDatabase?.close()
                    field?.writableDatabase?.close()
                    field?.close()
                }
                field = value
            }

        @JvmStatic
        internal inline val android.content.Context.pref: DBPreference
            get() {
                return preference?.let { pref ->
                    if (!pref.readableDatabase.isOpen || !pref.writableDatabase.isOpen) {
                        preference = null
                        DBPreference(this@pref).also { new ->
                            preference = new
                        }
                    } else {
                        pref
                    }
                } ?: DBPreference(this@pref).also { new ->
                    preference = new
                }
            }
    }
}