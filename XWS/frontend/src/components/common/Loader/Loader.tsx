import CircularProgress from "@mui/material/CircularProgress";
import Box from "@mui/material/Box";

const Loader = () => {
  return (
    <Box
      sx={{
        position: "fixed",
        zIndex: "8",
        display: "flex",
        justifyContent: "center", // Center horizontally
        alignItems: "center", // Center vertically
        height: "100vh", // Set the container height to full viewport height
        width: "100vw",
        top: "0",
        left: "0",
        backgroundColor: "rgba(0,0,0,.5)",
      }}
    >
      <CircularProgress />
    </Box>
  );
};

export default Loader;
