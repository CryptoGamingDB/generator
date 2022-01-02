<#import "skeleton.ftlh" as s>
<@s.pre/>
<h2>blogs</h2>
<br/>
<#list blogs as blog>
<h4><a href="${blog.url()}">${blog.name()}</a></h4>
</#list>
<@s.post/>
