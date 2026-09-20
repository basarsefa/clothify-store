import FormInput from "../form-input/form-input.component";
import { useState } from "react";
import "./login-form.style.scss";
import Button from "../button/button.component";
import api from "../../axiosInstance";
import { useGoogleLogin } from "@react-oauth/google";

const defaultFormFields = {
  email: "",
  password: "",
};

const LoginForm = () => {
  const [formFields, setFormFields] = useState(defaultFormFields);
  const { email, password } = formFields;

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormFields({ ...formFields, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await api.post("/auth/v1/login", formFields);
      console.log("token:", response.data);
    } catch (error) {}
  };

  const googleLogin = useGoogleLogin({
    onSuccess: async (tokenResponse) => {
      try {
        const payload = {
          accessToken: tokenResponse.access_token,
        };

        const response = await api.post("/auth/v1/google-login", payload);
        console.log("jwtToken: ", response.data);
      } catch (error) {
        console.log("hata: ", error);
      }
    },
  });

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
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
          <Button type="submit">SIGN IN</Button>
          <Button
            buttonType="google"
            type="submit"
            onClick={googleLogin}
          ></Button>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
