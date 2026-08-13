// App.test.jsx

import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import App from "/src/pages/App";
import { createMemoryRouter, RouterProvider } from "react-router";

describe("App component", () => {
  it("Renders one main element", () => {
    const routes = [
      {
        path:'/', 
        element: <App />
      }
    ]
    const router = createMemoryRouter(routes);
    render(<RouterProvider router={router}></RouterProvider>);
    expect(screen.getByRole('main')).toBeInTheDocument();
  });
});
