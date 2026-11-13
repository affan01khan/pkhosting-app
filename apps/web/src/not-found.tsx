import { Link } from "@tanstack/react-router";

import { useRootScreens } from "./root-screens";
import type { NotFoundScreenProps } from "./root-screens";

const PlainNotFound = ({ texts }: NotFoundScreenProps) => (
  <div className="flex flex-col items-start gap-2 p-8">
    <h1 className="font-semibold">{texts.notFound}</h1>
    <p>{texts.notFoundDescription}</p>
    <Link to="/" className="underline underline-offset-4">
      {texts.home}
    </Link>
  </div>
);

export const NotFound = () => {
  const { screens, texts } = useRootScreens();
  const Screen = screens.NotFound ?? PlainNotFound;

  return <Screen texts={texts} />;
};
