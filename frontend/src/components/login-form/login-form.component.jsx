import FormInput from "../form-input/form-input.component";
import { useState } from "react";
import "./login-form.style.scss";
import Button from "../button/button.component";
import api from "../../axiosInstance";
import { useGoogleLogin } from "@react-oauth/google";
import ForgotPasswordDialog from "../forgot-password-dialog/forgot-password-dialog.component";

const defaultFormFields = {
  email: "",
  password: "",
};

const LoginForm = () => {
  const [formFields, setFormFields] = useState(defaultFormFields);
  const { email, password } = formFields;
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormFields({ ...formFields, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");
    setLoading(true);

    try {
      const response = await api.post("/auth/login", formFields);
      const token = response.data.data;
      localStorage.setItem("token", token);
      setSuccessMessage("Giriş Başarılı!");
      resetForm();
    } catch (error) {
      const data = error?.response?.data;
      const backendMessage = data?.message || "Bir hata oluştu!";
      const httpStatus = error?.response?.status || data?.status;
      const requestId = data?.requestId || "none";

      console.log(data);
      if (httpStatus >= 500) {
        setErrorMessage(`${backendMessage} (Hata referans kodu: ${requestId})`);
      } else {
        setErrorMessage(backendMessage);
      }
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormFields(defaultFormFields);
  };

  const googleLogin = useGoogleLogin({
    onSuccess: async (tokenResponse) => {
      setErrorMessage("");
      setSuccessMessage("");
      setLoading(true);
      try {
        const payload = {
          accessToken: tokenResponse.access_token,
        };

        const response = await api.post("/auth/google-login", payload);
        const token = response.data.data;
        localStorage.setItem("token", token);
        setSuccessMessage("Google ile giriş başarılı!");
      } catch (error) {
        const data = error?.response?.data;
        const backendMessage = data?.message || "Bir hata oluştu";
        const httpStatus = error?.response?.status || data?.status;
        const requestId = data?.requestId || "none";

        if (httpStatus >= 500) {
          setErrorMessage(
            `${backendMessage} (Hata referans kodu: ${requestId})`,
          );
        } else {
          setErrorMessage(backendMessage);
        }
      } finally {
        setLoading(false);
      }
    },
    scope: "openid email profile",
  });

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
        <h2>Already have an account? </h2>
        <span>Sign in with your email and password.</span>

        {errorMessage && (
          <div className="alert alert-danger">{errorMessage}</div>
        )}
        {successMessage && (
          <div className="alert alert-success">{successMessage}</div>
        )}

        <FormInput
          label="Email"
          type="email"
          required
          name="email"
          value={email}
          onChange={handleChange}
        />
        <FormInput
          label="Password"
          type="password"
          required
          name="password"
          value={password}
          onChange={handleChange}
        />

        <div className="buttons-container">
          <Button type="submit" disabled={loading}>
            SIGN IN
          </Button>
          <Button
            buttonType="google"
            type="button"
            onClick={googleLogin}
            disabled={loading}
          >
            GOOGLE SIGN IN
          </Button>
        </div>
        <div className="forgot-password-link">
          <span onClick={() => !loading && setIsModalOpen(true)}>
            Şifremi Unuttum
          </span>
        </div>
      </form>
      {isModalOpen && (
        <ForgotPasswordDialog onClose={() => setIsModalOpen(false)} />
      )}
    </div>
  );
};

export default LoginForm;
