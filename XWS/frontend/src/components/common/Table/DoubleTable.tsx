import React, { useState } from "react";
import Box from "@mui/material/Box";
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import { Button } from "@mui/material";
import Skeleton from "@mui/material/Skeleton";

interface CollapsibleTableProps {
  data: Array<{ [key: string]: any }> | null | undefined;
  columns: Array<ColumnDefinition>;
  collapseColumn?: string;
  collapseColumns?: Array<ColumnDefinition>;
  onButtonClick?: (rowData: any) => void;
  onColumnButtonClick?: (rowData: any) => void;
}

interface ColumnDefinition {
  key: string;
  text: string;
  value?: (rowData: any) => any; // Function to calculate value
  label?: string;
  function?: (rowData: any) => void; // Function for button click
}

function DoubleTable({
  data,
  columns,
  collapseColumn,
  collapseColumns,
  onColumnButtonClick,
}: CollapsibleTableProps) {
  const [openRows, setOpenRows] = useState<string[]>([]);

  const handleRowClick = (rowKey: string) => {
    if (collapseColumn) {
      setOpenRows((prevOpenRows) =>
        prevOpenRows.includes(rowKey)
          ? prevOpenRows.filter((key) => key !== rowKey)
          : [...prevOpenRows, rowKey]
      );
    }
  };

  const renderCellValue = (row: any, column: ColumnDefinition) => {
    if (column.key.includes("id")) {
      return (
        <Button onClick={() => column.function && column.function(row)}>
          {column.label}
        </Button>
      );
    } else if (column.key === "image") {
      return (
        <img src={row[column.key]} alt="Item Image" width={50} height={50} />
      );
    } else if (typeof column.value === "function") {
      return column.value(row); // Apply the provided calculation function
    } else if (typeof row[column.key] === "boolean") {
      return row[column.key] ? "🟢" : "🔴";
    } else {
      return row[column.key];
    }
  };

  return (
    <TableContainer
      component={Paper}
      style={{ boxShadow: "none" }}
      className="tableRow"
    >
      <Table aria-label="collapsible table">
        <TableHead style={{ borderBottom: "1px solid rgba(0,0,0,0.1)" }}>
          <TableRow>
            {collapseColumn && <TableCell />}
            {columns.map((column) => (
              <TableCell key={column.key}>{column.text}</TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {data === null ||
          data === undefined ||
          data.length === 0 ||
          data[0] === null ? (
            <TableRow>
              <TableCell sx={{ padding: "0" }} colSpan={columns.length + 1}>
                <Skeleton
                  variant="text"
                  animation="wave"
                  width="100%"
                  height="80px"
                />
              </TableCell>
            </TableRow>
          ) : (
            data.map((row, index) => (
              <React.Fragment key={index}>
                <TableRow
                  sx={{ "& > *": { borderBottom: "unset" } }}
                  onClick={() => handleRowClick(row.key || index.toString())}
                >
                  {collapseColumn && (
                    <TableCell>
                      <IconButton aria-label="expand row" size="small">
                        {openRows.includes(row.key || index.toString()) ? (
                          <KeyboardArrowUpIcon />
                        ) : (
                          <KeyboardArrowDownIcon />
                        )}
                      </IconButton>
                    </TableCell>
                  )}
                  {columns.map((column: ColumnDefinition) => (
                    <TableCell key={column.key}>
                      {renderCellValue(row, column)}
                    </TableCell>
                  ))}
                </TableRow>
                {collapseColumn &&
                  (row[collapseColumn]?.length ?? 0) > 0 &&
                  openRows.includes(row.key || index.toString()) && (
                    <TableRow>
                      <TableCell
                        style={{ paddingBottom: 0, paddingTop: 0 }}
                        colSpan={columns.length + 1}
                      >
                        <Collapse in={true} timeout="auto" unmountOnExit>
                          <Box
                            sx={{
                              width: " 100%",
                              borderLeft: "1px solid rgba(0,0,0,0.1)",
                              borderRight: "1px solid rgba(0,0,0,0.1)",
                            }}
                          >
                            <Table
                              aria-label="purchases"
                              className="custom-table"
                            >
                              <TableBody>
                                {row[collapseColumn]?.map(
                                  (item: any, itemIndex: number) => (
                                    <TableRow
                                      key={itemIndex}
                                      style={{
                                        borderTop: "1px solid rgba(0,0,0,0.1)",
                                      }}
                                    >
                                      {collapseColumns &&
                                        collapseColumns.map((column: any) => (
                                          <TableCell
                                            key={column.key}
                                            style={{
                                              border: "none",
                                            }}
                                          >
                                            {column.key === "id" ? (
                                              <Button
                                                onClick={() =>
                                                  onColumnButtonClick &&
                                                  onColumnButtonClick(item)
                                                }
                                              >
                                                {column.label}
                                              </Button>
                                            ) : (
                                              renderCellValue(item, column)
                                            )}
                                          </TableCell>
                                        ))}
                                    </TableRow>
                                  )
                                )}
                              </TableBody>
                            </Table>
                          </Box>
                        </Collapse>
                      </TableCell>
                    </TableRow>
                  )}
              </React.Fragment>
            ))
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default DoubleTable;
