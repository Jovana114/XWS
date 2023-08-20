import { Routes, Route, Navigate } from "react-router-dom";
import Register from "./components/pages/Register";
import Login from "./components/pages/Login";
import Missing from "./components/pages/Missing";
import Unauthorized from "./components/pages/Unauthorized";
import RequireAuth from "./auth/RequireAuth";
import { useContext } from "react";
import { AuthContext } from "./auth/AuthContext";
import Home from "./components/pages/Home";
import Default from "./components/pages/Default";
import Host from "./components/pages/Host";
import Layout from "./components/pages/Layout";
import Users from "./components/pages/Users";
import "./style/App.css";
import Profile from "./components/pages/Profile";
import Accommodation from "./components/pages/Accommodation";
import Appointment from "./components/pages/Appointment";
import AppointmentReservation from "./components/pages/AppointmentReservation";

const App: React.FC = () => {
  const { auth } = useContext(AuthContext);

  return (
    <Routes>
      {/* public routes */}
      <Route
        path="/signin"
        element={
          !auth.accessToken ? (
            <Layout>
              <Login />
            </Layout>
          ) : (
            <Navigate to="/" />
          )
        }
      />
      <Route
        path="/signup"
        element={
          <Layout>
            <Register />
          </Layout>
        }
      />
      <Route
        path="/unauthorized"
        element={
          <Layout>
            <Unauthorized />
          </Layout>
        }
      />

      {/* Default/Home routes */}
      <Route
        path="/"
        element={<Layout>{auth.accessToken ? <Home /> : <Default />}</Layout>}
      />

      {/* protected routes */}
      <Route element={<RequireAuth allowedRoles={["ROLE_GUEST"]} />}>
        <Route
          path="/users"
          element={
            <Layout>
              <Users />
            </Layout>
          }
        />
        
        <Route
          path="/reserve"
          element={
            <Layout>
              <AppointmentReservation />
            </Layout>
          }
        />
      </Route>

      <Route element={<RequireAuth allowedRoles={["ROLE_HOST"]} />}>
        <Route
          path="/host"
          element={
            <Layout>
              <Host />
            </Layout>
          }
        />
        <Route
          path="/accommodation"
          element={
            <Layout>
              <Accommodation />
            </Layout>
          }
        />
        <Route
          path="/appointment"
          element={
            <Layout>
              <Appointment />
            </Layout>
          }
        />
      </Route>

      <Route
        element={<RequireAuth allowedRoles={["ROLE_GUEST", "ROLE_HOST"]} />}
      >
        <Route
          path="/profile"
          element={
            <Layout>
              <Profile />
            </Layout>
          }
        />
      </Route>

      {/* catch all */}
      <Route
        path="*"
        element={
          <Layout>
            <Missing />
          </Layout>
        }
      />
    </Routes>
  );
};

export default App;
