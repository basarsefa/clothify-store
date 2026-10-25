import RegisterForm from "../../components/register-form/register-form.component";
import LoginForm from "../../components/login-form/login-form.component";
import "./login.style.scss";

const Login = () => {
  return (
    <div className="login-page">
      <LoginForm />
      <RegisterForm />
    </div>
  );
};

export default Login;
