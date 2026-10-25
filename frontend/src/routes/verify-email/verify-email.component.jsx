import { useEffect, useState, useRef } from "react";
import { useSearchParams } from "react-router";
import api from "../../axiosInstance";
import "./verify-email.style.scss";

const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState("loading"); // loading, success, error
  const [message, setMessage] = useState(
    "E-posta adresiniz doğrulanıyor, lütfen bekleyin...",
  );
  const isRequestSent = useRef(false);

  // URL'deki (?token=...) parametresini alıyoruz
  const token = searchParams.get("token");

  useEffect(() => {
    if (!token) {
      setStatus("error");
      setMessage("Geçersiz veya eksik doğrulama anahtarı (token).");
      return;
    }
    if (isRequestSent.current) return;
    isRequestSent.current = true;

    const verifyToken = async () => {
      try {
        // Arka plandaki Spring Boot API'sine istek atıyoruz
        const response = await api.get(`/auth/verify-email?token=${token}`);

        setStatus("success");
        setMessage(response?.data?.message || "İşlem başarılı!");
      } catch (error) {
        const data = error?.response?.data;
        const backendMessage = data?.message || "Bir hata oluştu.";
        const httpStatus = error?.response?.status || data?.status;
        const requestId = data?.requestId || "none";
        setStatus("error");

        if (httpStatus >= 500) {
          setMessage(`${backendMessage} (Hata referans kod: ${requestId})`);
        } else {
          setMessage(backendMessage);
        }

        console.error(data);
      }
    };

    verifyToken();
  }, [token]);

  // Sayfa Tasarımı (Basit ve şık bir bilgilendirme ekranı)
  return (
    <div className="verify-email-container">
      <div className="verify-email-card">
        {status === "loading" && (
          <h2 className="loading">⏳ Lütfen Bekleyin</h2>
        )}
        {status === "success" && <h2 className="success">✅ Tebrikler!</h2>}
        {status === "error" && <h2 className="error">❌ Hata Oluştu</h2>}

        <p>{message}</p>
      </div>
    </div>
  );
};

export default VerifyEmail;
