<#import "skeleton.ftlh" as s>
<@s.pre/>
<h2>games</h2>
<br/>
<#list games as game>
<h4><a href="/games/${game.slug()}">${game.name()}</a></h4>
<p><#if chains[game.slug()]?has_content>${chains[game.slug()]?join(" ")} / </#if>${game.status()}
</#list>
<@s.post/>
