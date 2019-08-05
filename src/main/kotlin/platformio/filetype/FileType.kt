package platformio.filetype

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import platformio.PlatformIO
import javax.swing.Icon

class FileType: FileType, FileTypeIdentifiableByVirtualFile {
    override fun getDefaultExtension(): String {
        return PlatformIO.INI
    }

    override fun getIcon(): Icon? {
        return PlatformIO.icon
    }

    override fun getCharset(file: VirtualFile, comntent: ByteArray): String? {
        return Charsets.UTF_8.name()
    }

    override fun getName(): String {
        return "PlatformIO INI File"
    }

    override fun getDescription(): String {
        return "main definitions of your PlatformIO Project"
    }

    override fun isBinary(): Boolean {
        return false
    }

    override fun isReadOnly(): Boolean {
        return false
    }

    override fun isMyFileType(file: VirtualFile): Boolean {
        return file.name == PlatformIO.INI
    }
}
