<#import "skeleton.ftlh" as s>
<@s.pre/>
<h2><a href="${game.website()}" style="font-family:alagard">${game.name()}</a></h2>
<br/>
<a href="${game.slug()}.png"><img height="300" width="100%" style="object-fit:cover" src="${game.slug()}.png" alt="${game.name()}"></a>
<br/>
<h4>status</h4>
<p>${game.status()}</p>
<#if game.contracts()?has_content>
<h4>contracts</h4>
<br/>
<#list game.contracts() as contract>
<p>${contract.contract()} / ${contract.chain()} / <a href="${contract.market()}">${contract.name()}</a>
</#list>
</#if>
<@s.post/>
