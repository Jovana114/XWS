import { useEffect, useState } from "react";
import useRatingAccommodation from "../../hooks/useRatingAccommodation";
import useHandlingReservation from "../../hooks/useHandlingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const RatingAccommodation = () => {
  
  const { accommodationRatingData, error, submitRating, getRating } = useRatingAccommodation(true);
  const [accommodationID, setAccommodationID] = useState(""); 
  const [rating, setRating] = useState(0);

  useEffect(() => {
    const accommodationId = "youraccommodationId";
    getRating(accommodationId);
  }, []);

  const handleRatingSubmit = async (e: any) => {
    e.preventDefault();
      submitRating(
        accommodationID,
        rating
      );    
  };


  const { data, cancelReservation } = useHandlingReservation();

  const handleCancelAppointment = (e: any) => {
    cancelReservation(e.id);
  };

  const columns = [
    { key: "accommodationID", text: "Accommodation ID" },
    {
      key: "id",
      text: "Cancel",
      label: "Cancel",
      function: (e: any) => handleCancelAppointment(e),
    },
  ];

  return (
    <div>
      
      <h1>Rating accommodations</h1>
      <br></br>
      <div>
        <form onSubmit={(e) => e.preventDefault()}>
          <input type="text" name="accommodationID" placeholder="Accommodation ID" />
          <input type="number" placeholder="Rating (1-5)" />
          <button onClick={() => { handleRatingSubmit("1", 2, "Great accommodation!")}}>
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

export default RatingAccommodation;
