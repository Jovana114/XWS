import { Link } from "react-router-dom";
import AccommodationsWithAppointments from "./AccommodationsWithAppointments";
import Typography from "@mui/material/Typography";
const Default = () => {
  document.title = "Home";
  return (
    <>
      <AccommodationsWithAppointments />
      <Typography align="center">
        For more detailed information, please{" "}
        <Link
          style={{ textDecoration: "none", color: "#2b79ff" }}
          to={"/signin"}
        >
          SIGN IN
        </Link>{" "}
        here
      </Typography>
    </>
  );
};

export default Default;
