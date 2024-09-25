import { X } from 'lucide-react';

export default function Navbar() {
  return (
    <nav className="bg-[#F5F5F5] border-border    ">
      <div className="maxWidth container mx-auto flex justify-between items-center pt-4 pb-4 pl-5 pr-5 md:pl-8 md:pr-8 ">
        <div className="flex items-center space-x-4">
          <img
            className="w-[137.1px]"
            src="https://cdn.prod.website-files.com/66182d420e634ac8b9279dd6/66182d420e634ac8b9279e4c_brand-logo-login.aae252d8.svg"
            alt=""
          />
        </div>

        <div className="flex items-center space-x-4">
          <a
            href="https://sipago.coop"
            target="_blank"
            rel="noopener noreferrer"
          >
            <X size={24} /> 
          </a>
        </div>
      </div>
    </nav>
  );
}
