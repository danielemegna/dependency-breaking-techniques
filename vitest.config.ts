import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    include: ["src/**/*.test.ts"],
    // Each exercise's failing test runs in its own file context, so a red
    // suite in one exercise never blocks the others.
    isolate: true,
  },
});
