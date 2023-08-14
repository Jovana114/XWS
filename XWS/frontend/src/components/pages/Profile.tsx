/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { ThemeProvider } from "@emotion/react";
import theme from "../../style/theme";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import "react-toastify/dist/ReactToastify.css";
import {
  ColumnStyles,
  Footer,
  Header,
  LowerColumnStyles,
  LowerRowContainerStyles,
  LowerRowStyles,
  Main,
  ParentContainerStyles,
  UpperRowStyles,
} from "../custom/ProfileStyle";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import VerticalLine from "../custom/VerticalLine";
import useUser from "../../hooks/useUser";

const Profile = () => {
  const {
    handleUpdateProfile,
    handleUpdateUsername,
    handleUpdatePassword,
    handleDelete,
    fetchUserData,
  } = useUser(); // Use the custom hook

  const [open, setOpen] = useState(false);

  useEffect(() => {
    async function fetchData() {
      const userData = await fetchUserData();
      setEmail(userData.email);
      setFirstName(userData.first_name);
      setLastName(userData.last_name);
      setAddress(userData.address);
      setUsername(userData.username);
    }

    fetchData();
  }, []);

  const [email, setEmail] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [address, setAddress] = useState("");
  const [username, setUsername] = useState("");

  // New state variables for password update
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <ThemeProvider theme={theme}>
      <ParentContainerStyles>
        <UpperRowStyles>
          <ColumnStyles>
            <Header>
              <Typography variant="h4">Update Profile</Typography>
            </Header>
            <Main>
              <Box
                component="form"
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  "& .MuiTextField-root": { my: 1, width: "300px" },
                }}
                noValidate
                autoComplete="off"
              >
                <FormControl variant="outlined">
                  <TextField
                    type="email"
                    id="email"
                    label="Email"
                    value={email}
                    required
                    onChange={(e) => setEmail(e.target.value)}
                    autoComplete="off"
                  />
                </FormControl>

                <FormControl variant="outlined">
                  <TextField
                    type="text"
                    id="first_name"
                    label="First Name"
                    value={first_name}
                    required
                    onChange={(e) => setFirstName(e.target.value)}
                  />
                </FormControl>

                <FormControl variant="outlined">
                  <TextField
                    type="text"
                    id="last_name"
                    label="Last Name"
                    value={last_name}
                    required
                    onChange={(e) => setLastName(e.target.value)}
                  />
                </FormControl>

                <FormControl variant="outlined">
                  <TextField
                    type="text"
                    id="address"
                    label="Address"
                    value={address}
                    required
                    onChange={(e) => setAddress(e.target.value)}
                  />
                </FormControl>
              </Box>
            </Main>
            <Footer>
              <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={() =>
                  handleUpdateProfile(email, first_name, last_name, address)
                }
              >
                Update Profile
              </Button>
            </Footer>
          </ColumnStyles>
          <VerticalLine />
          <ColumnStyles>
            <Header>
              <Typography align="center" variant="h4">
                Update Username
              </Typography>
            </Header>
            <Main>
              {" "}
              <Box
                component="form"
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  "& .MuiTextField-root": { my: 1, width: "300px" },
                }}
                noValidate
                autoComplete="off"
              >
                <FormControl variant="outlined">
                  <TextField
                    type="text"
                    id="username"
                    label="Username"
                    value={username}
                    required
                    onChange={(e) => setUsername(e.target.value)}
                    autoComplete="off"
                  />
                </FormControl>
              </Box>
            </Main>
            <Footer>
              <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={() => handleUpdateUsername(username)}
              >
                Update Username
              </Button>
            </Footer>
          </ColumnStyles>
          <VerticalLine />
          <ColumnStyles>
            <Header>
              <Typography align="center" variant="h4">
                Update Password
              </Typography>
            </Header>
            <Main>
              <Box
                component="form"
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  "& .MuiTextField-root": { my: 1, width: "300px" },
                }}
                noValidate
                autoComplete="off"
              >
                <FormControl variant="outlined">
                  <TextField
                    type="password"
                    id="old_password"
                    label="Enter Old Password"
                    value={oldPassword}
                    required
                    onChange={(e) => setOldPassword(e.target.value)}
                    autoComplete="off"
                  />
                </FormControl>

                <FormControl variant="outlined">
                  <TextField
                    type="password"
                    id="new_password"
                    label="Enter New Password"
                    value={newPassword}
                    required
                    onChange={(e) => setNewPassword(e.target.value)}
                    autoComplete="off"
                  />
                </FormControl>

                <FormControl variant="outlined">
                  <TextField
                    type="password"
                    id="confirm_password"
                    label="Confirm New Password"
                    value={confirmPassword}
                    required
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    autoComplete="off"
                  />
                </FormControl>
              </Box>
            </Main>
            <Footer>
              <Button
                variant="contained"
                color="primary"
                fullWidth
                onClick={() =>
                  handleUpdatePassword(
                    oldPassword,
                    newPassword,
                    confirmPassword
                  )
                }
              >
                Update Password
              </Button>
            </Footer>
          </ColumnStyles>
        </UpperRowStyles>
        <LowerRowContainerStyles>
          <LowerRowStyles>
            <LowerColumnStyles>
              <Header>
                <Typography align="center" variant="h4">
                  Delete Account
                </Typography>
              </Header>
              <Main>
                <Button
                  variant="contained"
                  color="primary"
                  fullWidth
                  onClick={handleOpen}
                >
                  Delete Account
                </Button>
              </Main>
            </LowerColumnStyles>
          </LowerRowStyles>
        </LowerRowContainerStyles>
      </ParentContainerStyles>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirm Account Deletion"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to delete your account? This action is
            irreversible and will delete all your data permanently.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleDelete} variant="contained" color="primary">
            Confirm Delete
          </Button>
        </DialogActions>
      </Dialog>
    </ThemeProvider>
  );
};

export default Profile;
