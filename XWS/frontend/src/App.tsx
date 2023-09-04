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
import Layout from "./components/pages/Layout";
import "./style/App.css";
import Profile from "./components/pages/Profile";
import Accommodation from "./components/pages/Accommodation";
import Appointment from "./components/pages/Appointment";
import AppointmentReservation from "./components/pages/AppointmentReservation";
import ApproveReservation from "./components/pages/ApproveReservation";
import GuestReservedAppointments from "./components/pages/GuestReservedAppointments";
import RatingHosts from "./components/pages/RatingHosts";
import AccommodationFilter from "./components/pages/AccommodationFilter";
import RatingAccommodation from "./components/pages/RatingAccommodation";
import Host from "./components/pages/Host";

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
        path="/approve_reservation"
        element={
          <Layout>
            <ApproveReservation />
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

      <Route
        path="/accommodation_filter"
        element={
          <Layout>
            <AccommodationFilter />
          </Layout>
        }
      />

      {/* protected routes for ROLE_GUEST */}
      <Route  element={
          <RequireAuth allowedRoles={["ROLE_GUEST"]}>
            <Layout>
              <Route
                path="/reserve"
                element={<AppointmentReservation />}
              />
              <Route
                path="/rating_hosts"
                element={<RatingHosts />}
              />
              <Route
                path="/rating_accommodation"
                element={<RatingAccommodation />}
              />
              <Route
                path="/reserved"
                element={<GuestReservedAppointments />}
              />
              <Route
                path="/accommodation_filter"
                element={<AccommodationFilter />}
              />
            </Layout>
          </RequireAuth>
        }
      />

      {/* protected routes for ROLE_HOST */}
      <Route
        element={
          <RequireAuth allowedRoles={["ROLE_HOST"]}>
            <Layout>
              <Route path="/host" element={<Host />} />
              <Route
                path="/accommodation"
                element={<Accommodation />}
              />
              <Route
                path="/appointment"
                element={<Appointment />}
              />
            </Layout>
          </RequireAuth>
        }
      />

      {/* protected routes for ROLE_GUEST or ROLE_HOST */}
      <Route
        element={<RequireAuth allowedRoles={["ROLE_GUEST", "ROLE_HOST"]}>
              <Route path="/profile" element={<Profile />} />
          </RequireAuth>
        }
      />


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
