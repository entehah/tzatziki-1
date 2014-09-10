package tzatziki.pdf.emitter;

import tzatziki.analysis.exec.model.Embedded;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.tag.TagViews;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.model.Markdown;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultEmitters {
    public void registerDefaults(EmitterContext context) {
        context.register(FeatureExec.class, new FeatureEmitter());
        context.register(ScenarioExec.class, new ScenarioEmitter());
        context.register(Markdown.class, new MarkdownEmitter());
        context.register(Steps.class, new StepsEmitter());
        context.register(Tags.class, new TagsEmitter());
        context.register(TagDictionary.class, new TagDictionaryEmitter());
        context.register(TagViews.class, new TagViewsEmitter());
        context.register(Embedded.class, new EmbeddedEmitter());
    }


}
