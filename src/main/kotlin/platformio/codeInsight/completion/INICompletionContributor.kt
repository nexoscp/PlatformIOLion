package platformio.codeInsight.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import ini4idea.IniLanguage
import ini4idea.lang.IniTokenTypes
import platformio.Section

class INICompletionContributor : CompletionContributor() {
    init {
        val provider = INICompletionProvider<CompletionParameters>()
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(IniTokenTypes.SECTION_NAME).withLanguage(IniLanguage.INSTANCE),
                provider )
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(IniTokenTypes.KEY_NAME).withLanguage(IniLanguage.INSTANCE),
                provider )
    }
}

class INICompletionProvider<V : CompletionParameters> : CompletionProvider<V>() {
    override fun addCompletions(completionParameters: V, processingContext: ProcessingContext, resultSet: CompletionResultSet) {
        Section.sections.forEach {
            resultSet.addElement(LookupElementBuilder.create(it))
        }
    }
}
