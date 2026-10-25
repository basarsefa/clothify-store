import "./button.style.scss";

const BUTTON_STYLE_CLASS = {
  google: "google-sign-in",
  inverted: "inverted",
};

const Button = ({ children, buttonType, ...otherProps }) => {
  return (
    <button
      className={`button-container ${BUTTON_STYLE_CLASS[buttonType]}`}
      {...otherProps}
    >
      {children}
    </button>
  );
};

export default Button;
