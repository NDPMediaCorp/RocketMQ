<!-- Static navbar -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">RocketMQ Cockpit</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <!--
                <li class="active"><a href="#">Home</a></li>
                -->
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Topic<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="cockpit/topic/">Manage Topics</a></li>
                        <li><a href="cockpit/topic-progress/">Topic Progress</a> </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Consumer Group<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="cockpit/consumer-group/">Manage Consumer Groups</a></li>
                        <li><a href="cockpit/consume-progress/">Consumer Group Progress </a> </li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Query Message<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="cockpit/message/">Message ID</a></li>
                        <li><a href="#">Message Key</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="cockpit/admin/user">Manage User</a></li>
                        <li><a href="cockpit/ip/">IP Mapping</a></li>
                        <li class="divider"></li>
                        <li><a href="cockpit/name-server/">Name Server List</a></li>
                        <li><a href="cockpit/name-server/kv">Name Server KV</a></li>
                        <li class="divider"></li>
                        <li><a href="console/">Console</a> </li>
                    </ul>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</nav>