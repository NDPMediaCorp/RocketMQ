$(document).ready(function() {
    $("#registerButton").click(function() {
        var password = $("#password").val();
        var confirmPassword = $("#confirmPassword").val();

        if (password == "") {
            alert("Password required");
        }

        if (password != confirmPassword) {
            alert("Password and confirm password does not match");
        }


    });
});