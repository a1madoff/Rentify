package com.example.rentingapp.tools;

import com.example.rentingapp.models.RecommendedListing;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Recommendations {
    // Hashmap from user ids to listing ids
    HashMap<String, Set<String>> usersToListings = new HashMap<>();
    HashMap<String, Set<String>> listingsToUsers = new HashMap<>();


    public void loadTestData() {
        Set<String> UserASet = new HashSet<>();
        UserASet.add("a");
        UserASet.add("c");
        UserASet.add("d");
        UserASet.add("e");
        UserASet.add("f");
        usersToListings.put("a", UserASet);

        Set<String> UserBSet = new HashSet<>();
        UserBSet.add("a");
        UserBSet.add("b");
        usersToListings.put("b", UserBSet);

        Set<String> UserCSet = new HashSet<>();
        UserCSet.add("a");
        UserCSet.add("c");
        UserCSet.add("d");
        UserCSet.add("e");
        UserCSet.add("f");
        usersToListings.put("c", UserCSet);

        Set<String> UserXSet = new HashSet<>();
        UserXSet.add("a");
        UserXSet.add("b");
        UserXSet.add("c");
        UserXSet.add("f");
        usersToListings.put("x", UserXSet);

        Set<String> usersWhoLikeA = new HashSet<>();
        usersWhoLikeA.add("a");
        usersWhoLikeA.add("b");
        usersWhoLikeA.add("c");
        usersWhoLikeA.add("x");
        listingsToUsers.put("a", usersWhoLikeA);

        Set<String> usersWhoLikeB = new HashSet<>();
        usersWhoLikeB.add("b");
        usersWhoLikeB.add("x");
        listingsToUsers.put("b", usersWhoLikeB);

        Set<String> usersWhoLikeC = new HashSet<>();
        usersWhoLikeC.add("a");
        usersWhoLikeC.add("c");
        usersWhoLikeC.add("x");
        listingsToUsers.put("c", usersWhoLikeC);

        Set<String> usersWhoLikeD = new HashSet<>();
        usersWhoLikeD.add("a");
        usersWhoLikeD.add("c");
        listingsToUsers.put("d", usersWhoLikeD);

        Set<String> usersWhoLikeE = new HashSet<>();
        usersWhoLikeE.add("a");
        usersWhoLikeE.add("c");
        listingsToUsers.put("e", usersWhoLikeE);

        Set<String> usersWhoLikeF = new HashSet<>();
        usersWhoLikeF.add("a");
        usersWhoLikeF.add("c");
        usersWhoLikeF.add("x");
        listingsToUsers.put("f", usersWhoLikeF);
    }

    public double getUserSimilarity(String user1ID, String user2ID) {
        // TODO: handle case where user1ID or user2ID is not in the hashmap
        Set<String> user1Listings = usersToListings.get(user1ID);
        Set<String> user2Listings = usersToListings.get(user2ID);

        Set<String> intersection = new HashSet<>(user1Listings);
        intersection.retainAll(user2Listings);

        if (intersection.size() == 0) {
            return Double.NaN;
        }

        int unionSize = user1Listings.size() + user2Listings.size() - intersection.size();

        return (double) intersection.size() / (double) unionSize;
    }

    public double getListingScore(String userID, String listingID) {
        Set<String> usersWhoLikeListing = listingsToUsers.get(listingID);

        double totalSimilarity = 0.0;
        for (String iterUserID : usersWhoLikeListing) {
            totalSimilarity += getUserSimilarity(userID, iterUserID);
        }

        return totalSimilarity / (double) usersWhoLikeListing.size();
    }

    public PriorityQueue<RecommendedListing> getRecommendations(String userID) {
        Comparator<RecommendedListing> comparator = new Comparator<RecommendedListing>() {
            @Override
            public int compare(RecommendedListing r1, RecommendedListing r2) {
                int compareValue = r1.getScore().compareTo(r2.getScore());
                if (compareValue > 0) { // TODO: can replace with Integer.compare
                    return -1;
                } else if (compareValue < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        PriorityQueue<RecommendedListing> priorityQueue = new PriorityQueue<>(listingsToUsers.keySet().size(), comparator);

        for (String listingID : listingsToUsers.keySet()) {
            if (!usersToListings.get(userID).contains(listingID)) {
                double score = getListingScore(userID, listingID);
                RecommendedListing recommendedListing = new RecommendedListing(listingID, score);
                priorityQueue.add(recommendedListing);
            }
        }

        return priorityQueue;
    }







//    ----------------------------------------------------------------------------------------------------

    public Set<String> getUserNeighborhood(String userID) {
        return getUserNeighborhood(userID, 0.01);
    }

    public Set<String> getUserNeighborhood(String userID, double threshold) {
        Set<String> allUsersSet = usersToListings.keySet();
        Set<String> neighbors = new HashSet<>();

        for (String iterUserID : allUsersSet) {
            if (!userID.equals(iterUserID)) {
                double userSimilarity = getUserSimilarity(userID, iterUserID);
                if (!Double.isNaN(userSimilarity) && userSimilarity >= threshold) {
                    neighbors.add(iterUserID);
                }
            }
        }

        // TODO: calling function must check if num neighbors == 0
        return neighbors;
    }
}
