import { useEffect } from "react";
import useRatingAccommodation from "../../hooks/useRatingAccommodation";
import useHandlingReservation from "../../hooks/useHandlingReservation";
import DoubleTable from "../common/Table/DoubleTable";

const RatingAccommodation = () => {
  const { accommodationRatingData, error, submitRating, getRating } = useRatingAccommodation(true);

  useEffect(() => {
    const accommodationId = "youraccommodationId";
    getRating(accommodationId);
  }, []);

  const handleRatingSubmit = (accommodationId: string, rating: number, review: string) => {
    submitRating(accommodationId, rating, review);
  };

  const { data, cancelReservation } = useHandlingReservation();

  const handleCancelAppointment = (e: any) => {
    cancelReservation(e.id);
  };

  const columns = [
    { key: "accommodationID", text: "Accommodation ID" },
    { key: "startDate", text: "Start Date" },
    { key: "endDate", text: "End Date" },
    { key: "numGuests", text: "Number of Guests" },
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
      {error && <div className="error-message">{error}</div>}
      <br></br>
      <div>
        {/* Display accommodation rating data */}
        <h2>Accommodation Rating Data</h2>
        <pre>{JSON.stringify(accommodationRatingData, null, 2)}</pre>
      </div>
      <br></br>
      <div>
        {/* Rating form */}
        <h2>Submit Rating</h2>
        <form onSubmit={(e) => e.preventDefault()}>
          <input type="text" placeholder="Reservation ID" />
          <input type="text" placeholder="Accommodation ID" />
          <input type="number" placeholder="Rating" />
          <button onClick={() => handleRatingSubmit("youraccommodationId", 5, "Great accommodation!")}>
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
