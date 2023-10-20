<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>

<@common.page>
    <div class="mb-1">
        Add new user
    </div>
    ${message?ifExists}
    <@login.login "/registration" true/>
</@common.page>