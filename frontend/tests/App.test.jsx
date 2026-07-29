// App.test.jsx

import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import App from "/src/pages/App";

describe("App component", () => {
  it("Renders one main element", () => {   
    render(<App />);
    expect(screen.getByRole('main')).toBeInTheDocument();
  });
});
