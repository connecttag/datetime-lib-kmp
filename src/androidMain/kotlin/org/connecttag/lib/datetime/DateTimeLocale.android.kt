package org.connecttag.lib.datetime

import java.util.Locale

internal actual fun platformDefaultLocaleTag(): String = Locale.getDefault().toLanguageTag()
