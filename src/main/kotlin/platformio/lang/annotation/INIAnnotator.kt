package platformio.lang.annotation

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import ini4idea.lang.psi.*
import platformio.PlatformIO
import platformio.Section

// https://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support/annotator.html
class INIAnnotator: Annotator {
    override fun annotate(element: PsiElement, annotationHolder: AnnotationHolder) {
        val file = element.containingFile.virtualFile //figure out we are looking at projects platformio.ini
        if(file.name == PlatformIO.INI && element is IniPsiElement) {
            val text = element.node.firstChildNode?.text
            if (text != null) {
                when (element) {
                    is IniSectionName -> {
                        val sectionName = text.substring(1, text.length - 1)
                        if (!Section.isValidSectionName(sectionName)) {
                            annotationHolder.createErrorAnnotation(element, "unknown section name")
                        }
                    }
                    is IniKeyName -> {
                        // for now, nothing to do
                    }
                    is IniValue -> {
                        // for now, nothing to do
                    }
                }
            }
        }
    }
}
