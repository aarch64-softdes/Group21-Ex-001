import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import StudentPage from "./pages/studentPage";

const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <StudentPage />
        </QueryClientProvider>
    );
}

export default App;
