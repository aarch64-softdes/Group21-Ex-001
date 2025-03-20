import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import StatusPage from "./pages/statusPage";

const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <StatusPage />
        </QueryClientProvider>
    );
}

export default App;
