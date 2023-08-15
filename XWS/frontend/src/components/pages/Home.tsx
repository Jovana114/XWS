import Guest from "./Guest";
import Host from "./Host";
import useAuth from "../../hooks/useAuth";
import AccommodationsWithAppointments from "./AccommodationsWithAppointments";

document.title = "Home";

const Home = () => {
  const auth = useAuth();

  return (
    <>
      {auth.auth.roles?.includes("ROLE_HOST") ? <Host /> : <Guest />}
      <AccommodationsWithAppointments />
    </>
  );
};

export default Home;
