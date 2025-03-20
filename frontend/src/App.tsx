import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import FacultyPage from "./pages/facultyPage";

const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <FacultyPage />
        </QueryClientProvider>
    );
}

export default App;
