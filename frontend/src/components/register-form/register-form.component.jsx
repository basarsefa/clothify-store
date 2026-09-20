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

  const resetFormFields = () => {
    setFormFields(defaultFormFields);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      alert("Şifreler birbiriyle uyuşmuyor.");
      return;
    }

    const requestData = {
      displayName,
      email,
      password,
    };

    try {
      const response = await api.post("/auth/v1/register", requestData);
      console.log(response.data);
      alert("Kayıt işlemi tamamlanmıştır.");
      resetFormFields();
    } catch (error) {
      console.error(error);
      if (error.response && error.response.data) {
        alert(error.response.data);
      } else {
        alert("Bir hata oluştu, lütfen tekrar deneyin.");
      }
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

        <Button type="submit">Sign Up</Button>
      </form>
    </div>
  );
};

export default RegisterForm;
