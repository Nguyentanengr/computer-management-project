package controller;

import dao.ComputerDAO;
import model.Computer;

import java.util.ArrayList;

public class SearchProduct {
    public static SearchProduct getInstance() {
        return new SearchProduct();
    }

    public ArrayList<Computer> searchAll(String content) {

        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getMachineCode().toLowerCase().contains(content.toLowerCase()) || computer.getMachineName().toLowerCase().contains(content.toLowerCase()) ||
            computer.getCPUName().toLowerCase().contains(content.toLowerCase()) || computer.getGraphicsCard().toLowerCase().contains(content.toLowerCase()) ||
            computer.getOrigin().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchMachineCode(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getMachineCode().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchMachineName(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getMachineName().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchQuantity(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getQuantity() > Integer.parseInt(content)) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchPrice(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getPrice() > Double.parseDouble(content)) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchRam(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getRAM().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchCpu(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getCPUName().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchRom(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getRom().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchCard(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getGraphicsCard().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchOrigin(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() != 1) continue;
            if (computer.getOrigin().toLowerCase().contains(content.toLowerCase())) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }

    public ArrayList<Computer> searchDeletedProducts(String content) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        ArrayList<Computer> resultListComputer = new ArrayList<>();
        listComputer = ComputerDAO.getInstance().selectAll();
        for (Computer computer : listComputer) {
            if (computer.getStatus() == 1) continue;
            if (computer.getStatus() != 1) {
                resultListComputer.add(computer);
            }
        }
        return resultListComputer;
    }
}
