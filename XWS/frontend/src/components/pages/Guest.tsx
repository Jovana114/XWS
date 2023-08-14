import useAccomodation from "../../hooks/useAccommodation";
import DoubleTable from "../common/Table/DoubleTable";

const Guest = () => {
  const data = useAccomodation();
  const columns = [
    { key: "benefits", text: "Benefits" },
    { key: "location", text: "Location" },
    { key: "max_guests", text: "Max number of guests" },
    { key: "min_guests", text: "Min number of guests" },
    { key: "name", text: "Name" },
  ];
  const collapseColumns = [
    { key: "start", text: "Start Date" },
    { key: "end", text: "End Date" },
  ];

  return (
    <DoubleTable
      data={data}
      columns={columns}
      collapseColumn="appointments"
      collapseColumns={collapseColumns}
    />
  );
};

export default Guest;
