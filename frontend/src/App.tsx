import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import ProgramPage from "./pages/programPage";

const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <ProgramPage />
        </QueryClientProvider>
    );
}

export default App;
