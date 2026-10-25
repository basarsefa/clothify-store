import { useState } from "react";
import FormInput from "../form-input/form-input.component";
import Button from "../button/button.component";
import api from "../../axiosInstance";
import "./forgot-password-dialog.style.scss";

const ForgotPasswordDialog = ({ onClose }) => {
  const [forgotEmail, setForgotEmail] = useState("");
  const [modalLoading, setModalLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleForgotPasswordSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setSuccessMessage("");
    setModalLoading(true);

    try {
      const response = await api.post("/auth/forgot-password", {
        email: forgotEmail,
      });

      setSuccessMessage(response?.data?.message || "İşlem Başarılı!");

      setForgotEmail("");

      setTimeout(() => {
        onClose();
      }, 2500); // İşlem başarılıysa ana sayfadaki modal kapatma fonksiyonunu tetikle
    } catch (error) {
      const data = error?.response?.data;
      const backendMessage = data?.message || "Bir hata oldu!";
      const httpStatus = error?.response?.status || data?.status;
      const requestId = data?.requestId || "none";

      console.error(data);

      if (httpStatus >= 500) {
        setErrorMessage(`${backendMessage} (Hata referans kod: ${requestId})`);
      } else {
        setErrorMessage(backendMessage);
      }
    } finally {
      setModalLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Şifremi Unuttum</h3>
        <p>
          Sisteme kayıtlı e-posta adresinizi girin. Size şifrenizi
          yenileyebileceğiniz 15 dakika geçerli güvenli bir link göndereceğiz.
        </p>
        {errorMessage && (
          <div className="alert alert-danger">{errorMessage}</div>
        )}
        {successMessage && (
          <div className="alert alert-success">{successMessage}</div>
        )}

        <form onSubmit={handleForgotPasswordSubmit}>
          <FormInput
            label="E-posta Adresi"
            type="email"
            required
            name="forgotEmail"
            value={forgotEmail}
            onChange={(e) => setForgotEmail(e.target.value)}
            disabled={modalLoading}
          />
          <div className="modal-buttons">
            <Button
              buttonType="inverted"
              type="button"
              onClick={onClose}
              disabled={modalLoading}
            >
              İPTAL
            </Button>
            <Button type="submit" disabled={modalLoading}>
              GÖNDER
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ForgotPasswordDialog;
