<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html class="login-bg">
<head>
	<link href="images/favicon.ico" rel="shortcut icon">
	<link href="images/favicon.gif" type="image/gif" rel="icon">
	
	<title>电力线路山火预警系统</title>
    
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
    <!-- bootstrap -->
    <link href="bootstrap/css/bootstrap/bootstrap.css" rel="stylesheet" />
    <link href="bootstrap/css/bootstrap/bootstrap-responsive.css" rel="stylesheet" />
    <link href="bootstrap/css/bootstrap/bootstrap-overrides.css" type="text/css" rel="stylesheet" />

    <!-- global styles -->
    <link rel="stylesheet" type="text/css" href="bootstrap/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="bootstrap/css/elements.css" />
    <link rel="stylesheet" type="text/css" href="bootstrap/css/icons.css" />

    <!-- libraries -->
    <link rel="stylesheet" type="text/css" href="bootstrap/css/lib/font-awesome.css" />
    
    <!-- this page specific styles -->
    <link rel="stylesheet" href="bootstrap/css/compiled/signin.css" type="text/css" media="screen" />

    <!-- open sans font -->
    <link href='http://fonts.useso.com/css?family=Open+Sans:300italic,400italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css' />

    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>
<body>


    <!-- background switcher 
    <div class="bg-switch visible-desktop">
        <div class="bgs">
            <a href="#" data-img="landscape.jpg" class="bg active">
                <img src="bootstrap/img/bgs/landscape.jpg" />
            </a>
            <a href="#" data-img="blueish.jpg" class="bg">
                <img src="bootstrap/img/bgs/blueish.jpg" />
            </a>            
            <a href="#" data-img="7.jpg" class="bg">
                <img src="bootstrap/img/bgs/7.jpg" />
            </a>
            <a href="#" data-img="8.jpg" class="bg">
                <img src="bootstrap/img/bgs/8.jpg" />
            </a>
            <a href="#" data-img="9.jpg" class="bg">
                <img src="bootstrap/img/bgs/9.jpg" />
            </a>
            <a href="#" data-img="10.jpg" class="bg">
                <img src="bootstrap/img/bgs/10.jpg" />
            </a>
            <a href="#" data-img="11.jpg" class="bg">
                <img src="bootstrap/img/bgs/11.jpg" />
            </a>
        </div>
    </div>

-->
    <div class="row-fluid login-wrapper">
       
            <div class="logo" >
            	<h1>南京炬名电力科技有限公司</h1>
            </div>
       

        <form class="span4 box" method="post" action="Login.action">
            <div class="content-wrap">
                <h6>电力线路山火预警系统</h6>
                <input class="span12" type="text" placeholder="用户名" name="loginname" id="loginname"  />
                <input class="span12" type="password" placeholder="密码" name="password" id="password" />
                
                <div class="remember">
                    <input id="remember-me" type="checkbox" />
                    <label for="remember-me">记住密码</label>
                </div>
                <input type="submit" class="btn-glow primary login" value="登录">
                
            </div>
        </form>
<!--
        <div class="span4 no-account">
            <p>Don't have an account?</p>
            <a href="signup.html">Sign up</a>
        </div>			-->
    </div>

	<!-- scripts -->
    <script src="bootstrap/js/jquery-latest.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="bootstrap/js/theme.js"></script>

    <!-- pre load bg imgs -->
    <script type="text/javascript">
        $(function () {
            // bg switcher
            var $btns = $(".bg-switch .bg");
            $btns.click(function (e) {
                e.preventDefault();
                $btns.removeClass("active");
                $(this).addClass("active");
                var bg = $(this).data("img");

                $("html").css("background-image", "url('bootstrap/img/bgs/" + bg + "')");
            });

        });
    </script>

</body>
</html>