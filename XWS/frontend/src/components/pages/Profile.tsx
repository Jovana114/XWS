import { useContext, useState, useEffect } from "react";
import axiosPrivate from "../../api/axios";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { ThemeProvider } from "@emotion/react";
import theme from "../../style/theme";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AuthContext } from "../../auth/AuthContext";
import CircularIndeterminate from "../common/Loader/CircularIndeterminate";
import VerticalLine from "../custom/VerticalLine";
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

const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const EMAIL_REGEX = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
const USERS_URL = "/users/";

document.title = "Profile";

const Profile = () => {
  const { auth, setAuth }: any = useContext(AuthContext);

  const [email, setEmail] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [address, setAddress] = useState("");
  const [username, setUsername] = useState("");

  // New state variables for password update
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [errorMsg, setErrorMsg] = useState(""); // Add setErrorMsg to state variables

  const [isLoading, setIsLoading] = useState(true);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    if (!auth || auth.loading) {
      // If auth data is not available or still loading, show loading message
      setIsLoading(true);
    } else {
      const fetchUserData = async () => {
        try {
          const response = await axiosPrivate.get(USERS_URL + auth.id);
          const userData = response.data;

          setEmail(userData.email);
          setFirstName(userData.first_name);
          setLastName(userData.last_name);
          setAddress(userData.address);
          setUsername(userData.username);

          setIsLoading(false); // Mark loading as false when the data is fetched successfully
        } catch (error) {
          toast.error("Failed to fetch user data");
          setIsLoading(false); // Mark loading as false on error as well
        }
      };

      fetchUserData(); // Call the fetchUserData function to make the request
    }
  }, [auth]);

  const handleUpdateProfile = async () => {
    // Validate email
    const validEmailFormat = EMAIL_REGEX.test(email);
    if (!validEmailFormat) {
      toast.error("Invalid email address");
      return;
    }

    try {
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/data`,
        {
          email,
          first_name,
          last_name,
          address,
        },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      toast.success("Profile updated successfully!");
    } catch (error) {
      toast.error("Failed to update profile.");
    }
  };

  const handleUpdateUsername = async () => {
    // Validate username
    const validUsernameFormat = USER_REGEX.test(username);
    if (!validUsernameFormat) {
      toast.error(
        "Username should be 4 to 24 characters. Must begin with a letter. Letters, numbers, underscores, hyphens allowed."
      );
      return;
    }

    try {
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/username`,
        { username },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      // Check if user is logged in before performing logout action
      if (auth.id) {
        logout();
      } else {
        toast.success("Username updated successfully!");
      }
    } catch (error) {
      toast.error("Failed to update username.");
    }
  };

  const handleUpdatePassword = async () => {
    // Fetch user ID from local storage
    const authDataString = localStorage.getItem("authData");
    if (!authDataString) {
      setErrorMsg("User data not found.");
      toast.error(errorMsg);
      return;
    }

    // Validate new password and confirm password
    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }

    if (newPassword === oldPassword) {
      toast.error("New password must be different from the old password.");
      return;
    }

    try {
      await axiosPrivate.put(
        USERS_URL + `${auth.id}/password`,
        { old_password: oldPassword, new_password: newPassword },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      toast.success("Password updated successfully!");
      setErrorMsg(""); // Clear the error message when the update is successful
    } catch (error: any) {
      const errorMessage =
        error.response && error.response.data
          ? error.response.data.message
          : "Failed to update password.";
      setErrorMsg(errorMessage); // Set the error message on update failure
      toast.error(errorMessage);
    }
  };

  const handleDelete = async () => {
    try {
      await axiosPrivate.delete(USERS_URL + auth.id);
      logout(); // Logout the user after successful account deletion
      toast.success("Account deleted successfully!");
    } catch (error) {
      toast.error("Failed to delete account.");
    }
  };

  const logout = () => {
    setAuth({});
    setIsLoading(true);
    location.reload();
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  if (isLoading) {
    return <CircularIndeterminate />;
  }

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
                onClick={handleUpdateProfile}
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
                onClick={handleUpdateUsername}
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
                onClick={handleUpdatePassword}
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
      <ToastContainer />
    </ThemeProvider>
  );
};

export default Profile;
