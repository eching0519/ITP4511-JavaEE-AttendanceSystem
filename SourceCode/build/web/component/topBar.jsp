<%@page import="com.bean.User"%>

<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="javascript:void(0);" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
            <a href="javascript:void(0);" class="bars"></a>
            <a class="navbar-brand" href="index.html">VTG Attendance System</a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="./handleUser?action=logout" role="button">
                        <i class="material-icons" title="Logout">exit_to_app</i>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>