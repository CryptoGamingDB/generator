<#import "skeleton.ftlh" as s>
<@s.pre/>
<h2 style="font-family:alagard">podcasts</h2>
<br/>
<#list podcasts as podcast>
    <h4><a href="${podcast.url()}">${podcast.name()}</a></h4>
    <p>${podcast.description()}</p>
</#list>
<@s.post/>
