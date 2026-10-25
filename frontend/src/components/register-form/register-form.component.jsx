import { useState } from "react";
import api from "../../axiosInstance";
import FormInput from "../form-input/form-input.component";
import "./register-form.style.scss";
import Button from "../button/button.component";

const defaultFormFields = {
  displayName: "",
  email: "",
  password: "",
  confirmPassword: "",
};

const RegisterForm = () => {
  const [formFields, setFormFields] = useState(defaultFormFields);
  const { displayName, email, password, confirmPassword } = formFields;
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const resetFormFields = () => {
    setFormFields(defaultFormFields);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");

    if (password !== confirmPassword) {
      setErrorMessage("Şifreler birbiriyle uyuşmuyor.");
      return;
    }

    const requestData = {
      displayName,
      email,
      password,
    };

    setLoading(true);

    try {
      const response = await api.post("/auth/register", requestData);
      const message = response?.data?.message;
      setSuccessMessage(message || "Kayıt işlemi başarılı!");
      resetFormFields();
    } catch (error) {
      const data = error?.response?.data;
      const backendMessage = data?.message || "Sistemsel bir hata oluştu.";
      const httpStatus = error?.response?.status || data?.status;
      const requestId = data?.requestId || "none";
      console.error("hata:", data);
      if (httpStatus >= 500) {
        setErrorMessage(`${backendMessage} (Hata referans kodu: ${requestId})`);
      } else {
        setErrorMessage(backendMessage);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormFields({ ...formFields, [name]: value });
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit}>
        <h2>Don't have an account? </h2>
        <span>Sign up with your email and password.</span>

        {errorMessage && (
          <div className="alert alert-danger">{errorMessage}</div>
        )}
        {successMessage && (
          <div className="alert alert-success">{successMessage}</div>
        )}

        <FormInput
          label="Name"
          type="text"
          required
          onChange={handleChange}
          value={displayName}
          name="displayName"
        />

        <FormInput
          label="Email"
          type="email"
          required
          onChange={handleChange}
          value={email}
          name="email"
        />

        <FormInput
          label="Password"
          type="password"
          required
          onChange={handleChange}
          value={password}
          name="password"
        />

        <FormInput
          label="Confirm password"
          type="password"
          required
          onChange={handleChange}
          value={confirmPassword}
          name="confirmPassword"
        />

        <Button type="submit" disabled={loading}>
          SİGN UP
        </Button>
      </form>
    </div>
  );
};

export default RegisterForm;
