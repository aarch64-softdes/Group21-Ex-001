import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";
import React from "react";

interface LoadingButtonProps
  extends React.ComponentPropsWithoutRef<typeof Button> {
  isLoading?: boolean;
  children: React.ReactNode;
}

const LoadingButton = React.forwardRef<HTMLButtonElement, LoadingButtonProps>(
  ({ isLoading = false, children, disabled, ...props }, ref) => {
    return (
      <Button ref={ref} disabled={isLoading || disabled} {...props}>
        {isLoading ? (
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          children
        )}
      </Button>
    );
  }
);

export default LoadingButton;
