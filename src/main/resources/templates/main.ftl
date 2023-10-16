<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>
<#import "parts/login.ftl" as logout>

<@common.page>
    <div>
        <@logout.logout/>
    </div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Enter the message"/>
            <input type="text" name="tag" placeholder="Tag"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Add</button>
        </form>
    </div>
    <div>List of messages</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter}">
        <button type="submit">Search</button>
    </form>
    <#list messages as message>
    <div>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <i>${message.tag}</i>
        <strong>${message.authorName}</strong>
    </div>
    <#else>
        No message
    </#list>
</@common.page>