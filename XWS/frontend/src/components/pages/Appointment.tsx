/* eslint-disable react-hooks/exhaustive-deps */
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Box,
  TextField,
  Button,
} from "@mui/material";
import { useState } from "react";
import useUser from "../../hooks/useUser";
import useAppointment from "../../hooks/useAppointment";

const Appointment = () => {
  const { accommodations } = useUser();
  const { createAppointment } = useAppointment();
  const [accommodationId, setAccommodationId] = useState("");
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());
  const [priceType, setPriceType] = useState("");
  const [pricePer, setPricePer] = useState("");
  const [autoReservation ,setAutoReservation] = useState(false)
  const [price, setPrice] = useState(0)

  const handleCreateAppointment = () => {
    createAppointment(
      accommodationId,
      start + ":00",
      end + ":00",
      priceType,
      pricePer,
      autoReservation,
      price
    );
  };

  return (
    <Box display="flex" flexDirection="column">
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="accommodations-label">Accommodations</InputLabel>
        <Select
          labelId="accommodations-label"
          id="accommodations"
          value={accommodationId}
          label="Accommodations"
          onChange={(e) => setAccommodationId(e.target.value)}
        >
          {accommodations.length !== 0 && accommodations.map((accommodation: any, index: number) => (
            <MenuItem key={index} value={accommodation.id}>
              {accommodation.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="PriceType-label">Price Type</InputLabel>
        <Select
          labelId="PriceType-label"
          id="PriceType"
          value={priceType}
          label="Price Type"
          onChange={(e) => setPriceType(e.target.value)}
        >
          <MenuItem value="Regular">
            Regular
          </MenuItem>
          <MenuItem value="Holiday">
            Holiday
          </MenuItem>
          <MenuItem value="Weekend">
            Weekend
          </MenuItem>
          <MenuItem value="Summertime">
            Summertime
          </MenuItem>
        </Select>
      </FormControl>
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="PricePer-label">Price For</InputLabel>
        <Select
          labelId="PricePer-label"
          id="PricePer"
          value={pricePer}
          label="Price Type"
          onChange={(e) => setPricePer(e.target.value)}
        >
          <MenuItem value="price_per_guest">
            Guest
          </MenuItem>
          <MenuItem value="price_per_accommodation">
            Accommodation
          </MenuItem>
        </Select>
      </FormControl>
      <FormControl fullWidth required sx={{ width: "200px", margin: "10px 0" }}>
        <InputLabel id="autoReservation-label">Auto Reservation</InputLabel>
        <Select
          labelId="autoReservation-label"
          id="autoReservation"
          value={autoReservation}
          label="Auto Reservation"
          onChange={(e) => setAutoReservation(e.target.value === "true" ? true : false)}
        >
          <MenuItem value="true">
            True
          </MenuItem>
          <MenuItem value="false">
            False
          </MenuItem>
        </Select>
      </FormControl>
      <TextField
        label="Start"
        type="datetime-local"
        value={start}
        onChange={(e) => setStart(e.target.value)}
        fullWidth
        required
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <TextField
        label="End"
        type="datetime-local"
        value={end}
        onChange={(e) => setEnd(e.target.value)}
        fullWidth
        required
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <TextField
        label="Price"
        type="number"
        value={price}
        onChange={(e) => setPrice(parseInt(e.target.value))}
        fullWidth
        required
        inputProps={{
          min: 0,
        }}
        sx={{ width: "200px", margin: "10px 0" }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={handleCreateAppointment}
      >
        Create Appointment
      </Button>
    </Box>
  );
};

export default Appointment;
