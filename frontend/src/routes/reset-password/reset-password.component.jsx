import { useState } from "react";
import { useSearchParams } from "react-router";
import FormInput from "../../components/form-input/form-input.component";
import Button from "../../components/button/button.component";
import api from "../../axiosInstance";
import "./reset-password.style.scss";

const defaultFormFields = {
  password: "",
  confirmPassword: "",
};

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const [formFields, setFormFields] = useState(defaultFormFields);
  const { password, confirmPassword } = formFields;
  const [loading, setLoading] = useState(false);

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  // 🎯 URL'deki e-postadan gelen token'ı çekiyoruz
  const token = searchParams.get("token");

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormFields({ ...formFields, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setErrorMessage("");
    setSuccessMessage("");

    // 1. Şifrelerin eşleşme kontrolü (Frontend Güvenliği)
    if (password !== confirmPassword) {
      setErrorMessage("Girdiğiniz şifreler birbiriyle eşleşmiyor.");
      return;
    }

    // 2. Token varlık kontrolü
    if (!token) {
      setErrorMessage("Şifre sıfırlama anahtarı (token) eksik veya geçersiz.");
      return;
    }

    setLoading(true);
    try {
      // 📦 Sizin backend DTO formatınız: { "token": "...", "newPassword": "..." }
      const payload = {
        token: token,
        newPassword: password,
      };

      const response = await api.post("/auth/reset-password", payload);
      setSuccessMessage(
        response?.data?.message || "Şifreniz başarıyla değiştirilmiştir.",
      );
      setFormFields(defaultFormFields);
    } catch (error) {
      const data = error?.response?.data;
      const backendMessage = data?.message || "Bir hata oluştu!";
      const httpStatus = error?.response?.status || data?.status;
      const requestId = data?.requestId || "none";
      console.error(data);

      if (httpStatus >= 500) {
        setErrorMessage(`${backendMessage} (Hata referans kodu: ${requestId})`);
      } else {
        setErrorMessage(backendMessage);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="reset-password-container">
      <div className="reset-password-card">
        <form onSubmit={handleSubmit}>
          <h2>Yeni Şifre Belirle</h2>
          {errorMessage && (
            <div className="alert alert-danger">{errorMessage}</div>
          )}

          {successMessage && (
            <div className="alert alert-success">{successMessage}</div>
          )}

          <FormInput
            label="Yeni Şifre"
            type="password"
            required
            name="password"
            value={password}
            onChange={handleChange}
          />

          <FormInput
            label="Yeni Şifre Tekrar"
            type="password"
            required
            name="confirmPassword"
            value={confirmPassword}
            onChange={handleChange}
          />

          <div className="submit-btn-container">
            <Button type="submit" disabled={loading}>
              ŞİFREYİ GÜNCELLE
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ResetPassword;
