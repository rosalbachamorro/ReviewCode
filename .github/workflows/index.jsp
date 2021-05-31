<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Vulnerable Application Login</title>
    <style>
      form {
        margin: 0 auto;
        width: 400px;
        padding: 1em;
        border: 1px solid #ccc;
        border-radius: 1em;
      }

      form div+div {
        margin-top: 1em;
      }

      label {
        display: inline-block;
        width: 90px;
        text-align: right;
      }

      input {
        font: 1em sans-serif;
        width: 300px;
        box-sizing: border-box;
        border: 1px solid #999;
      }

      input:focus {
        border-color: #000;
      }

      .button {
        padding-left: 90px;
      }

      button {
        margin-left: .5em;
      }
    </style>
    <script>
      function validateForm() {
          var userId = document.form.user.value;
          var name = document.form.name.value;

          if (userId == "" || userId.includes("alert") || userId.includes("</")) {
              alert("Please enter a valid Windows User ID. You entered " + userId);
              document.form.user.focus();
              return false;
          } else if (name == "" || name.includes("alert") || name.includes("</")) {
              alert("Please enter a valid name. You entered " + name);
              document.form.name.focus();
              return false;
          }
      }
    </script>
  </head>
  <body>
	  <form name="form" action="VulnerableServlet" method="post" onsubmit="return validateForm()">
      <div>
        <label for="user">User ID:</label>
        <input type="text" name="user">
      </div>
      <div>
        <label for="name">Your Name:</label>
        <input type="text" name="name">
      </div>
      <div>
        <input type="hidden" id="forName" name="forName" value="oracle.jdbc.driver.OracleDriver">
      </div>
      <div>
        <input type="hidden" id="corpTaxAppURL" name="corpTaxAppURL" value="http://example.com/pnc/employeePortalW2Gen.jsp">
      </div>
      <div class="button">
        <button type="submit">Submit</button>
      </div>
	  </form>
  </body>
</html>
