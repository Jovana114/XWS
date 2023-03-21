import React from 'react'
import ResponsiveAppBar from '../drawer/ResponsiveAppBar'
import BasicTable from '../table/BasicTable'

export default function Home() {
  document.title = "Home"
  return (
    <>
      <ResponsiveAppBar />
      <BasicTable />
    </>
  )
}
