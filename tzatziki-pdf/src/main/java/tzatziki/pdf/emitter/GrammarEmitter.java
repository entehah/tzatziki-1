package tzatziki.pdf.emitter;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.ITextUtils;
import gutenberg.itext.Styles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.java.ClassEntry;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.java.GrammarVisitor;
import tzatziki.analysis.java.KeywordBasedPattern;
import tzatziki.analysis.java.MethodEntry;
import tzatziki.analysis.java.PackageEntry;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarEmitter implements Emitter<Grammar> {

    private Logger log = LoggerFactory.getLogger(GrammarEmitter.class);

    @Override
    public void emit(Grammar grammar, final ITextContext context) {
        grammar.traverse(new GrammarVisitor() {
            @Override
            public void visit(PackageEntry packageEntry, ClassEntry classEntry, MethodEntry methodEntry) {
                Styles styles = context.styles();

                Font keywordFont = styles.getFontOrDefault(StepsEmitter.STEP_KEYWORD_FONT);
                Font regexFont = styles.getFont(Styles.INLINE_CODE_FONT).or(inlineCodeFont(styles));
                BaseColor regexBg = styles.getColor(Styles.INLINE_CODE_BACKGROUND).or(BaseColor.LIGHT_GRAY);

                Paragraph p = new Paragraph();
                p.setSpacingBefore(5f);
                if(!Strings.isNullOrEmpty(methodEntry.comment())) {
                    p.add(new Chunk(methodEntry.comment(), styles.defaultFont()));
                    p.add(Chunk.NEWLINE);
                }

                for (KeywordBasedPattern kwPattern : methodEntry.keywordBasedPatterns()) {
                    Chunk keywordChunk = new Chunk(kwPattern.getKeyword(), keywordFont);
                    Chunk regexChunk = new Chunk(kwPattern.getPattern(), regexFont);
                    regexChunk.setBackground(regexBg);

                    p.add(keywordChunk);
                    p.add(new Chunk(" "));
                    p.add(regexChunk);
                    p.add(Chunk.NEWLINE);
                }

                context.append(p);
            }
        });
    }

    private Supplier<? extends Font> inlineCodeFont(final Styles styles) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                try {
                    return new Font(ITextUtils.inconsolata(), styles.defaultFontSize());
                } catch (Exception e) {
                    log.warn("Fail to retrieve font", e);
                    return FontFactory.getFont(FontFactory.COURIER, styles.defaultFontSize());
                }
            }
        };
    }
}
