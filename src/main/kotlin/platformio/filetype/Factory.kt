package platformio.filetype

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.util.io.ByteSequence
import com.intellij.openapi.vfs.VirtualFile
import platformio.PlatformIO.fileType
import platformio.PlatformIO.INI

class Factory : FileTypeFactory(), FileTypeRegistry.FileTypeDetector {
    override fun getVersion(): Int {
        return 1
    }

    override fun detect(file: VirtualFile, p1: ByteSequence, p2: CharSequence?): FileType? {
        if(file.name == INI) {
            return fileType
        }
        return null
    }

    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(fileType, INI)
    }
}
