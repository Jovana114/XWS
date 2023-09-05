import { useEffect } from "react";
import useRating from "../../hooks/useRating";
import DoubleTable from "../common/Table/DoubleTable";
import useHandlingReservation from "../../hooks/useHandlingReservation";

const RatingHosts = () => {
  const { submitRating, getRating } = useRating(true);

  useEffect(() => {
    const hostId = "yourHostId";
    getRating(hostId);
  }, []);

  const { data } = useHandlingReservation();


  const handleRatingSubmit = (hostId: string, rating: number, review: string) => {
    submitRating(hostId, rating, review);
  };

  const columns = [
    { key: "hostID", text: "Host ID" },
    {
      key: "id",
      text: "Cancel",
      label: "Cancel",
      function: (e: any) => handleCancelAppointment(e),
    },
  ];

  return (
    <div>
      <h1>Rating Hosts</h1>
     
      
      <div>
      
        <form onSubmit={(e) => e.preventDefault()}>
          <input type="text" placeholder="Host ID" />
          <input type="number" placeholder="Rating (1-5)" />
          <button onClick={() => handleRatingSubmit("yourHostId", 5, "Great host!")}>
            Submit
          </button>
        </form>
      </div>
      <br></br>
      <h2>My Reservations</h2>
      <DoubleTable data={data} columns={columns} />
    </div>
  );
};

export default RatingHosts;

function handleCancelAppointment(e: any) {
  throw new Error("Function not implemented.");
}

